export interface BuildInfoDto {
  readonly version: string;
  readonly hash: string;
  readonly buildTime: string;
  readonly environment: string;
}
