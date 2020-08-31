import { Component, Input } from "@angular/core";
import { faMars } from "@fortawesome/free-solid-svg-icons/faMars";
import { faVenus } from "@fortawesome/free-solid-svg-icons/faVenus";

import { GenderDto } from "../../../dto/participation";

@Component({
             selector: "app-gender",
             templateUrl: "./gender.component.html",
           })
export class GenderComponent {

  readonly GenderDto = GenderDto;
  readonly faVenus = faVenus;
  readonly faMars = faMars;

  @Input()
  gender = GenderDto.MALE;
}
