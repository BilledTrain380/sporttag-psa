import { IconDefinition } from "@fortawesome/fontawesome-common-types";
import { faCog, faRunning, faTrophy } from "@fortawesome/free-solid-svg-icons";
import uuid from "uuid/v4";

import { ROLE_ADMIN, ROLE_USER } from "../../@security/auth-constants";

export interface PageMenu {
  readonly id: string;
  readonly title: string;
  readonly icon: IconDefinition;
  readonly link: string;
  readonly requiredAuthorities: ReadonlyArray<string>;
}

export const MENU_ITEMS: ReadonlyArray<PageMenu> = [
  {
    id: uuid(),
    title: $localize`Ranking`,
    icon: faTrophy,
    link: "/pages/ranking",
    requiredAuthorities: [ROLE_USER],
  },
  {
    id: uuid(),
    title: $localize`Athletics`,
    icon: faRunning,
    link: "/pages/athletics",
    requiredAuthorities: [ROLE_USER],
  },
  {
    id: uuid(),
    title: $localize`Administration`,
    icon: faCog,
    link: "/pages/administration",
    requiredAuthorities: [ROLE_ADMIN],
  },
];
