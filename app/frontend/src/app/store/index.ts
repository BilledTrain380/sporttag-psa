import { ActionReducerMap, MetaReducer } from "@ngrx/store";

import { environment } from "../../environments/environment";

import { AppState } from "./app";
import { athleticsReducer } from "./athletics/athletics.reducer";
import { eventSheetsReducer } from "./event-sheets/event-sheets.reducer";
import { groupReducer } from "./group/group.reducer";
import { menuReducer } from "./menu/menu.reducer";
import { participationReducer } from "./participation/participation.reducer";
import { rankingReducer } from "./ranking/ranking.reducer";
import { userManagementReducer } from "./user-management/user-management.reducer";
import { userReducer } from "./user/user.reducer";

export const reducers: ActionReducerMap<AppState> = {
  activeMenu: menuReducer,
  user: userReducer,
  group: groupReducer,
  participation: participationReducer,
  userManagement: userManagementReducer,
  athletics: athleticsReducer,
  eventSheets: eventSheetsReducer,
  ranking: rankingReducer,
};

export const metaReducers: Array<MetaReducer<AppState>> = !environment.production ? [] : [];
