export const LANGUAGES: ReadonlyArray<Language> = [
  {
    locale: "en",
    name: "English",
  },
  {
    locale: "de",
    name: "Deutsch",
  },
];

export interface Language {
  readonly locale: string;
  readonly name: string;
}
