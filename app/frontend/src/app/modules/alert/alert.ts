import { Injectable } from "@angular/core";

export interface Alert {
  readonly type: AlertType;
  readonly message: string;

  isSuccess(): boolean;

  isInfo(): boolean;

  isWarning(): boolean;

  isDanger(): boolean;

  isPrimary(): boolean;

  isSecondary(): boolean;
}

export enum AlertType {
  SUCCESS = "SUCCESS",
  INFO = "INFO",
  WARNING = "WARNING",
  DANGER = "DANGER",
  PRIMARY = "PRIMARY",
  SECONDARY = "SECONDARY",
}

@Injectable({
              providedIn: "root",
            })
export class AlertFactory {
  textAlert(): AlertInstance {
    return new TextAlertInstance();
  }
}

export abstract class AlertInstance {
  success(message: string): Alert {
    return this.createInstance(AlertType.SUCCESS, message);
  }

  info(message: string): Alert {
    return this.createInstance(AlertType.INFO, message);
  }

  warning(message: string): Alert {
    return this.createInstance(AlertType.WARNING, message);
  }

  error(message: string): Alert {
    return this.createInstance(AlertType.DANGER, message);
  }

  primary(message: string): Alert {
    return this.createInstance(AlertType.PRIMARY, message);
  }

  secondary(message: string): Alert {
    return this.createInstance(AlertType.SECONDARY, message);
  }

  protected abstract createInstance(type: AlertType, message: string): Alert;
}

export class TextAlertInstance extends AlertInstance {
  protected createInstance(type: AlertType, message: string): Alert {
    return new TextAlert(type, message);
  }
}

export abstract class AbstractAlert implements Alert {
  protected constructor(
    readonly type: AlertType,
    readonly message: string,
  ) {
  }

  isDanger(): boolean {
    return this.type === AlertType.DANGER;
  }

  isInfo(): boolean {
    return this.type === AlertType.INFO;
  }

  isPrimary(): boolean {
    return this.type === AlertType.PRIMARY;
  }

  isSecondary(): boolean {
    return this.type === AlertType.SECONDARY;
  }

  isSuccess(): boolean {
    return this.type === AlertType.SUCCESS;
  }

  isWarning(): boolean {
    return this.type === AlertType.WARNING;
  }
}

export class TextAlert extends AbstractAlert {
  constructor(
    type: AlertType,
    message: string,
  ) {
    super(type, message);
  }
}
