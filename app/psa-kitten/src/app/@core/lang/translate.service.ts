import {TranslateLoader} from '@ngx-translate/core';
import {Observable, of} from 'rxjs';
import {LANG_DE} from './lang.de';
import {LANG_EN} from './lang.en';

const languages = {
    lang_de: LANG_DE,
    lang_en: LANG_EN,
};

/**
 * Translate loader which loads directly JSON from a ts file.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export class JSONTranslateLoader implements TranslateLoader {

    getTranslation(lang: string): Observable<any> {

        const language: object|undefined = languages[lang];

        if (language === undefined) return of(languages['lang_en']);

        return of(language);
    }
}
