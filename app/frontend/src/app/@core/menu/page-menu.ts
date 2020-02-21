import { IconDefinition } from "@fortawesome/fontawesome-common-types";

export interface PageMenu {
  readonly id: string;
  readonly title: string;
  readonly icon?: IconDefinition;
  readonly link?: string;
  readonly children?: ReadonlyArray<PageMenu>;
}
