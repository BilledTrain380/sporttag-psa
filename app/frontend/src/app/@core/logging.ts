// tslint:disable: no-any no-console ban-types

export function getLogger(name: string): Logger {
  return new ConsoleLogger(name);
}

export interface Logger {
  debug(message: any, ...optionalParams: ReadonlyArray<any>): void;

  info(message: any, ...optionalParams: ReadonlyArray<any>): void;

  log(message: any, ...optionalParams: ReadonlyArray<any>): void;

  warn(message: any, ...optionalParams: ReadonlyArray<any>): void;

  error(message: any, ...optionalParams: ReadonlyArray<any>): void;
}

export class ConsoleLogger implements Logger {
  constructor(
    private readonly name: string,
  ) {
  }

  debug(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.debug, message, ...optionalParams);
  }

  info(message: string, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.info, message, ...optionalParams);
  }

  log(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.log, message, ...optionalParams);
  }

  warn(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.warn, message, ...optionalParams);
  }

  error(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.error, message, ...optionalParams);
  }

  private logMessage(logFn: Function, message: any, ...optionalParams: ReadonlyArray<any>): void {
    logFn(new Date().toUTCString(), `[${this.name}] - ${message}`, ...optionalParams);
  }
}
