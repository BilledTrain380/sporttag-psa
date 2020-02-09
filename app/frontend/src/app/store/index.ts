import { ActionReducerMap, MetaReducer, } from "@ngrx/store";

import { environment } from "../../environments/environment";

import { menuReducer } from "./menu/menu.reducer";
import { userReducer } from "./user/user.reducer";
import { AppState } from "./app";

export const reducers: ActionReducerMap<AppState> = {
  activeMenu: menuReducer,
  user: userReducer,
};

export const metaReducers: Array<MetaReducer<AppState>> = !environment.production ? [] : [];
