import { Component, OnDestroy, OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { combineLatest, Observable, Subject } from "rxjs";
import { map, startWith, takeUntil } from "rxjs/operators";

import { BALLWURF, BALLZIELWURF, KORBEINWURF, SCHNELLLAUF, SEILSPRINGEN, WEITSPRUNG } from "../../dto/athletics";
import { GenderDto, ParticipationStatusType } from "../../dto/participation";
import {
  loadCompetitorsAction,
  loadGroupsAction,
  loadParticipationStatusAction,
  updateCompetitorResultAction
} from "../../store/athletics/athletics.action";
import { selectCompetitors, selectGroups, selectParticipationStatus } from "../../store/athletics/athletics.selector";
import { VOID_PROPS } from "../../store/standard-props";

import { CompetitorModel } from "./athletics-models";
import { GenderFilterFormValues, genderFilterPredicate, initialGenderFilterFormValues } from "./filter/gender-filter";
import { initialStatusFilterFormValues, StatusFilterFormValues, statusFilterPredicate } from "./filter/status-filter";

@Component({
             selector: "app-athletics",
             templateUrl: "./athletics.component.html",
             styleUrls: ["./athletics.component.scss"],
           })
export class AthleticsComponent implements OnInit, OnDestroy {
  readonly GenderDto = GenderDto;

  readonly disciplines: ReadonlyArray<string> = [
    SCHNELLLAUF,
    WEITSPRUNG,
    BALLWURF,
    BALLZIELWURF,
    SEILSPRINGEN,
    KORBEINWURF,
  ];

  get genderFilterGroup(): FormGroup {
    return (this.filterForm?.controls.filter as FormGroup).controls.gender as FormGroup;
  }

  get statusFilterGroup(): FormGroup {
    return (this.filterForm?.controls.filter as FormGroup).controls.status as FormGroup;
  }

  get groupControl(): AbstractControl {
    return this.filterForm?.controls[this.formControls.group]!;
  }

  get disciplineControl(): AbstractControl {
    return this.filterForm?.controls[this.formControls.discipline]!;
  }

  get genderFilterCount(): number {
    return Object.values(this.genderFilterGroup.value)
      .filter(it => it).length;
  }

  get statusFilterCount(): number {
    return Object.values(this.statusFilterGroup.value)
      .filter(it => it).length;
  }

  readonly formControls = {
    filter: {
      gender: {
        genderMale: "genderMale",
        genderFemale: "genderFemale",
      },
      status: {
        statusPresent: "statusPresent",
        statusAbsent: "statusAbsent",
      },
    },
    group: "group",
    discipline: "discipline",
  };
  filterForm?: FormGroup;

  isParticipationOpen$?: Observable<boolean>;
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
    const filterForm = this.formBuilder.group({
                                                gender: this.formBuilder.group(initialGenderFilterFormValues),
                                                status: this.formBuilder.group(initialStatusFilterFormValues),
                                              });

    this.filterForm = this.formBuilder.group({
                                               [this.formControls.group]: "",
                                               [this.formControls.discipline]: this.disciplines[0],
                                               filter: filterForm,
                                             });

    this.groupControl.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(group => this.store.dispatch(loadCompetitorsAction({group})));

    this.isParticipationOpen$ = this.store.select(selectParticipationStatus)
      .pipe(map(status => status.type === ParticipationStatusType.OPEN));

    this.groups$ = this.store.select(selectGroups)
      .pipe(map(groups => {
        if (groups.length > 0) {
          this.groupControl.setValue(groups[0].name);
        }

        return groups.map(group => group.name);
      }));
    this.competitors$ = combineLatest([
                                        this.disciplineControl.valueChanges.pipe(startWith(this.disciplines[0])),
                                        this.genderFilterGroup.valueChanges.pipe(startWith(initialGenderFilterFormValues)),
                                        this.statusFilterGroup.valueChanges.pipe(startWith(initialStatusFilterFormValues)),
                                        this.store.select(selectCompetitors),
                                      ])
      .pipe(map(sources => {
        this.competitorsUpdate$.next();

        // tslint:disable: no-magic-numbers
        const discipline = sources[0];
        const genderFilters: GenderFilterFormValues = sources[1];
        const statusFilters: StatusFilterFormValues = sources[2];
        const competitors = sources[3];
        // tslint:enable

        const genderPredicate = genderFilterPredicate(genderFilters);
        const statusPredicate = statusFilterPredicate(statusFilters);

        return competitors
          .filter(competitor => genderPredicate(competitor) && statusPredicate(competitor))
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

    this.store.dispatch(loadParticipationStatusAction(VOID_PROPS));
    this.store.dispatch(loadGroupsAction(VOID_PROPS));
  }

  ngOnDestroy(): void {
    this.competitorsUpdate$.complete();
    this.destroy$.complete();
  }

  trackByModel(_: number, item: CompetitorModel): number {
    return item.hashcode();
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
