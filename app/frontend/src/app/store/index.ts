import { ActionReducerMap, MetaReducer } from "@ngrx/store";

import { environment } from "../../environments/environment";

import { AppState } from "./app";
import { groupReducer } from "./group/group.reducer";
import { menuReducer } from "./menu/menu.reducer";
import { participationReducer } from "./participation/participation.reducer";
import { userReducer } from "./user/user.reducer";

export const reducers: ActionReducerMap<AppState> = {
  activeMenu: menuReducer,
  user: userReducer,
  group: groupReducer,
  participation: participationReducer,
};

export const metaReducers: Array<MetaReducer<AppState>> = !environment.production ? [] : [];
