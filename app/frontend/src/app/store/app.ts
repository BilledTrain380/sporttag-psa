import { GroupState } from "./group/group.reducer";
import { MenuState } from "./menu/menu.reducer";
import { UserState } from "./user/user.reducer";

export interface AppState {
  readonly activeMenu: MenuState;
  readonly user: UserState;
  readonly group: GroupState;
}
