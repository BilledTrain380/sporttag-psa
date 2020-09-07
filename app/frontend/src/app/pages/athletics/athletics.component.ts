import { Component, OnDestroy, OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { combineLatest, Observable, Subject } from "rxjs";
import { map, startWith, takeUntil } from "rxjs/operators";

import { FormControlsObject } from "../../@core/forms/form-util";
import { BALLWURF, BALLZIELWURF, KORBEINWURF, SCHNELLLAUF, SEILSPRINGEN, WEITSPRUNG } from "../../dto/athletics";
import { loadCompetitorsAction, loadGroupsAction, updateCompetitorResultAction } from "../../store/athletics/athletics.action";
import { selectCompetitors, selectGroups } from "../../store/athletics/athletics.selector";
import { VOID_PROPS } from "../../store/standard-props";

import { CompetitorModel } from "./athletics-models";

@Component({
             selector: "app-athletics",
             templateUrl: "./athletics.component.html",
             styleUrls: ["./athletics.component.scss"],
           })
export class AthleticsComponent implements OnInit, OnDestroy {
  readonly disciplines: ReadonlyArray<string> = [
    SCHNELLLAUF,
    WEITSPRUNG,
    BALLWURF,
    BALLZIELWURF,
    SEILSPRINGEN,
    KORBEINWURF,
  ];

  get groupControl(): AbstractControl {
    return this.filterForm?.controls[this.formControls.group]!;
  }

  get disciplineControl(): AbstractControl {
    return this.filterForm?.controls[this.formControls.discipline]!;
  }

  readonly formControls: FormControlsObject = {
    group: "group",
    discipline: "discipline",
  };
  filterForm?: FormGroup;

  groups$?: Observable<ReadonlyArray<string>>;
  competitors$?: Observable<ReadonlyArray<CompetitorModel>>;

  private readonly competitorsUpdate$ = new Subject();
  private readonly destroy$ = new Subject();

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly store: Store,
  ) {
  }

  ngOnInit(): void {
    this.filterForm = this.formBuilder.group({
                                               [this.formControls.group]: "",
                                               [this.formControls.discipline]: this.disciplines[0],
                                             });

    this.groupControl.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(group => this.store.dispatch(loadCompetitorsAction({group})));

    this.groups$ = this.store.select(selectGroups)
      .pipe(map(groups => {
        if (groups.length > 0) {
          this.groupControl.setValue(groups[0].name);
        }

        return groups.map(group => group.name);
      }));
    this.competitors$ = combineLatest([
                                        this.disciplineControl.valueChanges.pipe(startWith(this.disciplines[0])),
                                        this.store.select(selectCompetitors),
                                      ])
      .pipe(map(sources => {
        this.competitorsUpdate$.next();

        const discipline = sources[0];
        const competitors = sources[1];

        return competitors
          .map(competitor => {
            const model = CompetitorModel.fromDtoWithDiscipline(competitor, discipline);

            model.result.displayValueControl.valueChanges
              .pipe(takeUntil(this.competitorsUpdate$))
              .subscribe(value => {
                if (value.includes(",")) {
                  model.result.displayValueControl.setValue(value.replace(",", "."));
                }
              });

            return model;
          });
      }));

    this.store.dispatch(loadGroupsAction(VOID_PROPS));
  }

  ngOnDestroy(): void {
    this.competitorsUpdate$.complete();
    this.destroy$.complete();
  }

  trackByPoints(_: number, item: CompetitorModel): number {
    return item.result.points;
  }

  updateCompetitorResultIfValid(competitorModel: CompetitorModel): void {
    if (competitorModel.result.displayValueControl.valid) {
      this.store.dispatch(updateCompetitorResultAction({
                                                         competitorId: competitorModel.id,
                                                         result: competitorModel.result.toResultElement(),
                                                       }));
    }
  }
}
