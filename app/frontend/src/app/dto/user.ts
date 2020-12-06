export interface UserRelation {
  readonly password: string;
}

export interface UserInput {
  readonly username: string;
  readonly enabled: boolean;
  readonly password: string;
}

export interface UserElement {
  readonly username?: string;
  readonly enabled?: boolean;
}

export interface UserDto {
  readonly id: number;
  readonly username: string;
  readonly authorities: ReadonlyArray<string>;
  readonly enabled: boolean;
  readonly locale: string;
  readonly password: string;
}
