import { UserDto } from "../../../../dto/user";

export class UserModel {
  constructor(
    readonly userId: number,
    readonly username: string,
    public isEnabled: boolean,
  ) {
  }

  static ofDto(userDto: UserDto): UserModel {
    return new UserModel(userDto.id, userDto.username, userDto.enabled);
  }
}
