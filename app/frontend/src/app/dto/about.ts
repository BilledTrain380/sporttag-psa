export interface BuildInfoDto {
  readonly version: string;
  readonly hash: string;
  readonly buildTime: string;
  readonly environment: string;
  readonly latestVersion: string;
}

export function isVersionUpToDate(buildInfo: BuildInfoDto): boolean {
  return buildInfo.version.localeCompare(buildInfo.latestVersion) >= 0;
}
