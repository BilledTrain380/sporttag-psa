import { IconDefinition } from "@fortawesome/fontawesome-common-types";
import { faCog, faRunning, faTrophy } from "@fortawesome/free-solid-svg-icons";
import uuid from "uuid/v4";

export interface PageMenu {
  readonly id: string;
  readonly title: string;
  readonly icon?: IconDefinition;
  readonly link?: string;
}

export const MENU_ITEMS: ReadonlyArray<PageMenu> = [
  {
    id: uuid(),
    title: "Ranking",
    icon: faTrophy,
    link: "/pages/ranking",
  },
  {
    id: uuid(),
    title: "Athletics",
    icon: faRunning,
    link: "/pages/athletics",
  },
  {
    id: uuid(),
    title: "Administration",
    icon: faCog,
    link: "/pages/administration"
  },
];
