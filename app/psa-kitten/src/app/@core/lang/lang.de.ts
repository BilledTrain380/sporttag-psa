
// tslint:disable: max-line-length
export const LANG_DE = {

    participation: {
        title: {
            default: 'Teilnahme',
            resetParticipation: 'Teilnahme zurücksetzen',
            groupsWithPendingParticipation: 'Gruppen mit noch offenen Teilnahmen',
        },
        status: {
            closed: 'Teilnahme ist abgeschlossen',
            open: 'Teilnahme ist offen',
        },
        action: {
            close: 'Abschliessen',
            reset: 'Zurücksetzen',
            lateParticipation: 'Nachmeldung',
        },
        alert: {
            resetSuccess: 'Die Teilnahme wurde zurückgesetzt.',
            closeParticipation: 'Wenn Sie die Teilnahme abschliessen, kann ein Teinehmer nur noch durch eine Ummeldung einer anderen Sportart zugewiesen werden.',
            resetParticipation: 'Wenn Sie die Teilnahme zurücksetzen werden alle Teilnehmer Daten unwiederruflich gelöscht.',
            confirmReset: 'Möchten Sie die Teilnahme wirklich zurücksetzen?',
        },
        text: {
            toCloseParticipation: 'Um die Leichtathletik starten zu können, müssen Sie die Teinahme abschliessen.<br>Dadurch werden die Startnummern generiert und Sie können die Wettkampfblätter herunterladen.',
            toResetParticipation: 'Um die Datenerfassung von neuem zu beginnen müssen Sie die Teilnahme zurücksetzen.<br>Dadurch werden alle Teilnehmer Daten gelöscht und Sie können die Teilnehmer von Anfang neu erfassen.',
            toManagement: 'Um die Teilnahme abzuschliessen klicken Sie',
            toManagementHere: 'hier',
        },
        label: {
            sports: 'Sportart',
        },
    },

    group: {
        title: {
            default: 'Gruppe',
            groups: 'Gruppen',
            overview: 'Übersicht',
            participants: 'Teilnehmer',
            csvFormat: 'CSV Format',
        },
        action: {
            import: 'Import',
        },
        status: {
            withPendingParticipation: 'offene Teilnehmer',
            withNoPendingParticipation: 'Teilnahme abgeschlossen',
            noGroups: 'Es sind keine Gruppen vorhanden.',
        },
        alert: {
            importSuccess: 'Gruppen wurden importiert.',
            fileUploadError: 'Die Datei konnte nicht hochgeladen werden.',
        },
        label: {
            actions: 'Aktionen',
        },
        text: {
            fileRequirements: '<li>Muss die Dateiendung <mark>.csv</mark> entsprechen</li>\n' +
                '        <li>\n' +
                '            Muss die folgende Reihenfolge der Attribute einhalten:<br>\n' +
                '            <i>Gruppe,Nachname,Vorname,Geschlecht,Strasse,PLZ,Ort,Geburtsdatum,Gruppenleiter</i>\n' +
                '        </li>\n' +
                '        <li>Das Geschlecht muss <mark>m</mark> für Knaben und <mark>w</mark> für Mädchen entsprechen</li>\n' +
                '        <li>Das Gebutsdatum muss dem Format <mark>dd.MM.YYYY</mark> entsprechen; z.B. 06.10.2009 </li>\n' +
                '        <li>Das Trennzeichen muss dem Komma (,) entsprechen</li>',
        },
    },

    eventPage: {
        title: {
            participantList: 'Teilnehmerliste',
            eventSheets: 'Wettkampfblätter',
        },
        action: {
            export: 'Export',
        },
        label: {
            all: 'alle',
        },
    },

    participant: {
        label: {
            prename: 'Vorname',
            surname: 'Nachname',
            gender: 'Geschlecht',
            birthday: 'Geburtstag',
            address: 'Adresse',
            zipCode: 'PLZ',
            town: 'Ortschaft',
            absent: 'Abwesend',
        },
        alert: {
            confirmDelete: 'Möchten Sie den Teilnehmer \'{{ name }}\' wirklich löschen?',
            createSuccess: 'Der Teilnehmer wurde erstellt.',
            saveSuccess: 'Der Teilnehmer wurde gespeichert.',
            deleteSuccess: 'Der Teilnehmer wurde gelöscht.',
        },
    },

    athleticsPage: {
        results: {
            title: {
                default: 'Resultate',
                competitors: 'Wettkämpfer',
                absentCompetitors: 'Abwesende Wettkämpfer',
            },
            label: {
                startNumber: 'Startnummer',
                surname: 'Nachname',
                prename: 'Vorname',
                gender: 'Geschlecht',
                result: 'Leistung',
                points: 'Punkte',
                distance: 'Distanz',
            },
            status: {
                noGroups: 'Keine Gruppen mit Wettkämpfer vorhanden',
            },
        },
        ranking: {
            title: {
                types: 'Ranglistentypen',
            },
            label: {
                all: 'alle',
                disciplines: 'Disziplinen',
                disciplineGroup: '3-Kampf',
                total: 'Gesamt',
                ubsCup: 'UBS Cup',
            },
            action: {
                export: 'Export',
            },
        },
    },

    settings: {
        users: {
            title: {
                default: 'Benutzerliste',
            },
            label: {
                username: 'Benutzername',
                enabled: 'Aktiviert',
                password: 'Passwort',
            },
            action: {
                create: 'Benutzer erstellen',
                changePassword: 'Password ändern',
            },
            alert: {
                confirmDelete: 'Möchten Sie den Benutzer "{{ username }}" wircklich löschen?',
            },
            text: {
                passwordRequirements: '<ul>\n' +
                    '                    <li>Muss zwischen 8 und 64 Zeichen lang sein</li>\n' +
                    '                    <li>Muss mindestens eine Zahl enthalten</li>\n' +
                    '                    <li>Muss mindestens einen Kleinbuchstaben enthalten</li>\n' +
                    '                    <li>Muss mindestens einen Grossbuchstaben enthalten</li>\n' +
                    '                    <li>Muss mindestens ein Sonderzeichen enthalten</li>\n' +
                    '                    <li>Darf keine Leerzeichen enthalten</li>\n' +
                    '                </ul>',
                noUsers: 'Es sind keine Benutzer vorhanden',
            },
            form: {
                validation: {
                    usernameIsTaken: 'Der Benutzername wird bereits genutzt',
                },
            },
        },
    },

    login: {
        text: {
            info: 'Bitte warten Sie einige Sekunden. Sollte die Weiterleitung nicht funktionieren klicken Sie',
            here: 'hier',
            redirectInfo: 'Sie werden zum Login weitergeleitet...',
            welcome: 'Willkommen zu PSA!',
            slogan: 'Ihr Sport Event Management System.',
            authProgress: 'Authentifizierung in Bearbeitung',
            wait: 'Bitte warten',
        },
    },

    alert: {
        great: 'Super!',
        info: 'Information!',
        attention: 'Achtung!',
        warning: 'Warnung!',
        error: 'Fehler!',
    },

    page: {
        groups: 'Gruppen',
        group: 'Gruppe {{ name }}',
        event: 'Veranstaltung',
        athletics: 'Leichtathletik',
        results: 'Resultate',
        ranking: 'Ranglisten',
        management: 'Verwaltung',
        settings: 'Einstellungen',
        users: 'Benutzer',
    },

    form: {
        validation: {
            required: 'Dieses Feld wird benötigt',
            pattern: 'Das Feld entspricht nicht den Anforderungen',
        },
        action: {
            abort: 'Abbrechen',
            save: 'Speichern',
            edit: 'Bearbeiten',
        },
    },

    label: {
        notDefined: 'N/A',
        actions: 'Aktionen',
        password: 'Passwort',
        changePassword: 'Passwort ändern',
        logout: 'Abmelden',
        login: 'Anmelden',
    },

    module: {
        confirmation: {
            confirm: 'Bestätigen',
        },
    },

    error: {
        noConnection: {
            title: 'Keine Verbindung zum Server :(',
            text: 'PSA kann keine Verbindung mit dem Server aufnehmen. Versuchen Sie es später noch einmal. Sollte das Problem weiterhin bestehen wenden Sie sich an den Administrator.',
            action: 'Erneut versuchen',
        },
        notFound: {
            title: '404 Seite nicht gefunden',
            text: 'Die Seite welche Sie aufrufen wollen existiert nicht.',
            action: 'Zum Home',
        },
    },

    // direct translations from a enum or a fix value in the application
    MALE: 'Männlich',
    FEMALE: 'Weiblich',
    Athletics: 'Leichtathletik',
};
