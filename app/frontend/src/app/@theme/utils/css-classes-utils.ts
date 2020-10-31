import { CSS_CLASS_COLUMN_PREFIX } from "../theme-constants";

export function buildColumnCssClass(size: number): string {
  return `${CSS_CLASS_COLUMN_PREFIX}${Math.floor(size)}`;
}
