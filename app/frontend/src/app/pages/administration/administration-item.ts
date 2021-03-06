import { faBuilding, faClipboard, faFileAlt, faUsers } from "@fortawesome/free-solid-svg-icons";
import uuid from "uuid/v4";

import { PageMenu } from "../../@core/menu/page-menu";

const ADMINISTRATION_PATH = "/pages/administration";

export const ADMINISTRATION_ITEMS: ReadonlyArray<PageMenu> = [
  {
    id: uuid(),
    link: `${ADMINISTRATION_PATH}/group-management`,
    title: $localize`Group Management`,
    icon: faClipboard,
    requiredAuthorities: ["ROLE_ADMIN"],
  },
  {
    id: uuid(),
    link: `${ADMINISTRATION_PATH}/participation-management`,
    title: $localize`Participation Management`,
    icon: faBuilding,
    requiredAuthorities: ["ROLE_ADMIN"],
  },
  {
    id: uuid(),
    link: `${ADMINISTRATION_PATH}/event-sheets-management`,
    title: $localize`Event Sheets`,
    icon: faFileAlt,
    requiredAuthorities: ["ROLE_ADMIN"],
  },
  {
    id: uuid(),
    link: `${ADMINISTRATION_PATH}/user-management`,
    title: $localize`User Management`,
    icon: faUsers,
    requiredAuthorities: ["ROLE_ADMIN"],
  },
];
