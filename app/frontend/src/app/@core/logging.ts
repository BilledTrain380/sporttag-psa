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
    this.logMessage(console.debug, "DEBUG", message, ...optionalParams);
  }

  info(message: string, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.info, "INFO", message, ...optionalParams);
  }

  log(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.log, "LOG", message, ...optionalParams);
  }

  warn(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.warn, "WARN", message, ...optionalParams);
  }

  error(message: any, ...optionalParams: ReadonlyArray<any>): void {
    this.logMessage(console.error, "ERROR", message, ...optionalParams);
  }

  private logMessage(logFn: Function, logLevel: string, message: any, ...optionalParams: ReadonlyArray<any>): void {
    logFn(new Date().toISOString(), `${logLevel} [${this.name}] - ${message}`, ...optionalParams);
  }
}
