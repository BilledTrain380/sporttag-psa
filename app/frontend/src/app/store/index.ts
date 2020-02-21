import { ActionReducerMap, MetaReducer } from "@ngrx/store";

import { environment } from "../../environments/environment";

import { AppState } from "./app";
import { menuReducer } from "./menu/menu.reducer";
import { userReducer } from "./user/user.reducer";

export const reducers: ActionReducerMap<AppState> = {
  activeMenu: menuReducer,
  user: userReducer,
};

export const metaReducers: Array<MetaReducer<AppState>> = !environment.production ? [] : [];
