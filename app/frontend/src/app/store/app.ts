import { AthleticsState } from "./athletics/athletics.reducer";
import { EventSheetsState } from "./event-sheets/event-sheets.reducer";
import { GroupState } from "./group/group.reducer";
import { MenuState } from "./menu/menu.reducer";
import { ParticipationState } from "./participation/participation.reducer";
import { UserManagementState } from "./user-management/user-management.reducer";
import { UserState } from "./user/user.reducer";

export interface AppState {
  readonly activeMenu: MenuState;
  readonly user: UserState;
  readonly group: GroupState;
  readonly participation: ParticipationState;
  readonly athletics: AthleticsState;
  readonly userManagement: UserManagementState;
  readonly eventSheets: EventSheetsState;
}
