import { AthleticsState } from "./athletics/athletics.reducer";
import { GroupState } from "./group/group.reducer";
import { MenuState } from "./menu/menu.reducer";
import { ParticipationState } from "./participation/participation.reducer";
import { UserState } from "./user/user.reducer";

export interface AppState {
  readonly activeMenu: MenuState;
  readonly user: UserState;
  readonly group: GroupState;
  readonly participation: ParticipationState;
  readonly athletics: AthleticsState;
}
