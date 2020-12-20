import { PsaLocale } from "../../../dto/profile";

export const LANGUAGES: ReadonlyArray<Language> = [
  {
    value: "en",
    locale: PsaLocale.EN,
    name: "English",
  },
  {
    value: "de",
    locale: PsaLocale.DE,
    name: "Deutsch",
  },
];

export interface Language {
  readonly value: string;
  readonly locale: PsaLocale;
  readonly name: string;
}
