import { ActionReducerMap, MetaReducer, } from "@ngrx/store";

import { environment } from "../../environments/environment";

import { menuReducer, MenuState } from "./menu/menu.reducer";
import { userReducer, UserState } from "./user/user.reducer";

export interface State {
  readonly activeMenu: MenuState;
  readonly user: UserState;
}

export const reducers: ActionReducerMap<State> = {
  activeMenu: menuReducer,
  user: userReducer,
};

export const metaReducers: Array<MetaReducer<State>> = !environment.production ? [] : [];
