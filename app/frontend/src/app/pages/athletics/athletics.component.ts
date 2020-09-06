import { Component, OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { combineLatest, Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";

import { FormControlsObject } from "../../@core/forms/form-util";
import { BALLWURF, BALLZIELWURF, KORBEINWURF, SCHNELLLAUF, SEILSPRINGEN, WEITSPRUNG } from "../../dto/athletics";
import { loadCompetitorsAction, updateCompetitorResultAction } from "../../store/athletics/athletics.action";
import { selectCompetitors } from "../../store/athletics/athletics.selector";

import { CompetitorModel } from "./athletics-models";

@Component({
             selector: "app-athletics",
             templateUrl: "./athletics.component.html",
             styleUrls: ["./athletics.component.scss"],
           })
export class AthleticsComponent implements OnInit {

  // FIXME: Load groups from backend
  readonly groups = ["2a", "2b"];
  readonly disciplines: ReadonlyArray<string> = [
    SCHNELLLAUF,
    WEITSPRUNG,
    BALLWURF,
    BALLZIELWURF,
    SEILSPRINGEN,
    KORBEINWURF,
  ];

  get disciplineControl(): AbstractControl {
    return this.filterForm?.controls[this.formControls.discipline]!;
  }

  readonly formControls: FormControlsObject = {
    group: "group",
    discipline: "discipline",
  };
  filterForm?: FormGroup;

  competitors$?: Observable<ReadonlyArray<CompetitorModel>>;

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

    this.competitors$ = combineLatest([
                                        this.disciplineControl.valueChanges.pipe(startWith(this.disciplines[0])),
                                        this.store.select(selectCompetitors),
                                      ])
      .pipe(map(sources => {
        const discipline = sources[0];
        const competitors = sources[1];

        const competitorModels = competitors
          .map(competitor => CompetitorModel.fromDtoWithDiscipline(competitor, discipline));

        competitorModels.forEach(model => model.result.displayValueControl.valueChanges
          .subscribe(value => {
            if (value.includes(",")) {
              model.result.displayValueControl.setValue(value.replace(",", "."));
            }
          }));

        return competitorModels;
      }));

    this.store.dispatch(loadCompetitorsAction({group: "2a"}));
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
