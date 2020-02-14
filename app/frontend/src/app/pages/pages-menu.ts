import { faAddressBook, faCalendar, faTrophy } from "@fortawesome/free-solid-svg-icons";
import uuid from "uuid/v4";

import { PageMenu } from "../@core/menu/page-menu";

export const MENU_ITEMS: ReadonlyArray<PageMenu> = [
  {
    id: uuid(),
    title: "Groups",
    icon: faAddressBook,
    link: "/pages/groups/overview",
  },
  {
    id: uuid(),
    title: "Event",
    icon: faCalendar,
    link: "/pages/event",
  },
  {
    id: uuid(),
    title: "Athletics",
    icon: faTrophy,
    children: [
      {
        id: uuid(),
        title: "Results",
        link: "/pages/athletics/results",
      },
    ],
  },
];
