import {NbMenuItem} from '@nebular/theme';
import {NbAccessChecker} from '@nebular/security';
import {Observable} from 'rxjs';

/*
 * In order to ACL the menu items, we add a object
 * to the data attribute. The object has a property canShow
 * which has the type signature: (accessChecker: NbAccessChecker).
 *
 * This allows ACL for the menu itmes. The page component will
 * change the hidden attribute based on the canShow property.
 */
export const MENU_ITEMS: NbMenuItem[] = [
    {
        title: 'Groups',
        icon: 'fa fa-address-book',
        link: '/pages/group/overview',
        home: true,
        data: {
            canShow: (it: NbAccessChecker): Observable<boolean> => it.isGranted('view', 'groups'),
            translation: 'page.groups',
        },
    },
    {
        title: 'Event',
        icon: 'fa fa-calendar',
        link: '/pages/event',
        data: {
            canShow: (it: NbAccessChecker): Observable<boolean> => it.isGranted('view', 'event'),
            translation: 'page.event',
        },
    },
    {
        title: 'Athletics',
        icon: 'fas fa fa-trophy',
        link: '/pages/athletics',
        data: {
            translation: 'page.athletics',
        },
        children: [
            {
                title: 'Results',
                link: '/pages/athletics/results',
                data: {
                    canShow: (it: NbAccessChecker): Observable<boolean> => it.isGranted('view', 'results'),
                    translation: 'page.results',
                },
            },
            {
                title: 'Ranking',
                link: '/pages/athletics/ranking',
                data: {
                    canShow: (it: NbAccessChecker): Observable<boolean> => it.isGranted('view', 'ranking'),
                    translation: 'page.ranking',
                },
            },
        ],
    },
    {
        title: 'Management',
        icon: 'fa fa-building',
        link: '/pages/management',
        data: {
            canShow: (it: NbAccessChecker): Observable<boolean> => it.isGranted('view', 'management'),
            translation: 'page.management',
        },
    },
    {
        title: 'Settings',
        icon: 'nb-gear',
        data: {
            canShow: (it: NbAccessChecker): Observable<boolean> => it.isGranted('view', 'settings'),
            translation: 'page.settings',
        },
        children: [
            {
                title: 'Users',
                link: '/pages/settings/users',
                data: {
                    translation: 'page.users',
                },
            },
        ],
    },
];
