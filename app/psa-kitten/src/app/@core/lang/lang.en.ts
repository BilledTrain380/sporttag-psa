
// tslint:disable: max-line-length
export const LANG_EN = {

    participation: {
        title: {
            default: 'Participation',
            resetParticipation: 'Reset Participation',
            groupsWithPendingParticipation: 'Groups with open participation',
        },
        status: {
            closed: 'Participation is closed',
            open: 'Participation is open',
        },
        action: {
            close: 'Close',
            reset: 'Reset',
            lateParticipation: 'Add Participant',
        },
        alert: {
            resetSuccess: 'Participation was reset.',
            closeParticipation: 'If you close the participation, participants can only change their sport trough a re-participation.',
            resetParticipation: 'If you reset the participation, all participant data will be deleted.',
            confirmReset: 'Do you want to reset the participation?',
        },
        text: {
            toCloseParticipation: 'To start the athletics you have to close the participation.<br>As a result, startnumbers will be generated and event sheets are available to download.',
            toResetParticipation: 'In order to start over with the sport event, you have to reset the participation.<br>As a result, all participant data will be deleted and you can start over with a new sport event.',
            toManagement: 'To close the participation click',
            toManagementHere: 'here',
        },
        label: {
            sports: 'Kind of Sport',
        },
    },

    group: {
        title: {
            default: 'Group',
            groups: 'Groups',
            overview: 'Overview',
            participants: 'Participants',
            csvFormat: 'CSV Format',
        },
        action: {
            import: 'Import',
        },
        status: {
            withPendingParticipation: 'Open participation',
            withNoPendingParticipation: 'Closed participation',
            noGroups: 'No groups are available.',
        },
        alert: {
            importSuccess: 'Groups are imported.',
            fileUploadError: 'The file could not be uploaded.',
        },
        label: {
            actions: 'Actions',
        },
        text: {
            fileRequirements: '<li>Must end with <mark>.csv</mark> extension</li>\n' +
                '        <li>\n' +
                '            Must follow the following attribute order:<br>\n' +
                '            <i>Group,Surname,Prename,Gender,Street,Zip Code,Town,Birthday,Group Coach</i>\n' +
                '        </li>\n' +
                '        <li>The gender must be a <mark>m</mark> for men and a <mark>w</mark> for woman</li>\n' +
                '        <li>The birthday must follow the format <mark>dd.MM.YYYY</mark>; e.g. 06.10.2009 </li>\n' +
                '        <li>The csv delimiter must be a comma (,)</li>',
        },
    },

    eventPage: {
        title: {
            participantList: 'Participantlist',
            eventSheets: 'Event Sheets',
        },
        action: {
            export: 'Export',
        },
        label: {
            all: 'all',
        },
    },

    participant: {
        label: {
            prename: 'Prename',
            surname: 'Surname',
            gender: 'Gender',
            birthday: 'Birthday',
            address: 'Address',
            zipCode: 'Zip Code',
            town: 'Town',
            absent: 'Absent',
        },
        alert: {
            confirmDelete: 'Do you want to delete the participant \'{{ name }}\'?',
            createSuccess: 'Participant is added.',
            saveSuccess: 'Participant is saved.',
            deleteSuccess: 'Participant is deleted.',
        },
    },

    athleticsPage: {
        results: {
            title: {
                default: 'Results',
                competitors: 'Competitors',
                absentCompetitors: 'Absent Competitors',
            },
            label: {
                startNumber: 'Startnumber',
                surname: 'Surname',
                prename: 'Prename',
                gender: 'Gender',
                result: 'Result',
                points: 'Points',
                distance: 'Distance',
            },
            status: {
                noGroups: 'No groups with competitors are available',
            },
        },
        ranking: {
            title: {
                types: 'Ranking Types',
            },
            label: {
                all: 'all',
                disciplines: 'Disciplines',
                disciplineGroup: 'Discipline Group',
                total: 'Total',
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
                default: 'Userlist',
            },
            label: {
                username: 'Username',
                enabled: 'Enabled',
                password: 'Password',
            },
            action: {
                create: 'Add User',
                changePassword: 'Change Password',
            },
            alert: {
                confirmDelete: 'Do you want to delete the user "{{ username }}"?',
            },
            text: {
                passwordRequirements: '<ul>\n' +
                    '                    <li>Must be between 8 and 64 characters</li>\n' +
                    '                    <li>Must contain at least 1 number</li>\n' +
                    '                    <li>Must contain at least 1 lower case character</li>\n' +
                    '                    <li>Must contain at least 1 upper case character</li>\n' +
                    '                    <li>Must contain at least 1 special character</li>\n' +
                    '                    <li>Must not contain any spaces</li>\n' +
                    '                </ul>',
                noUsers: 'No users available',
            },
            form: {
                validation: {
                    usernameIsTaken: 'The username is already in use',
                },
            },
        },
    },

    login: {
        text: {
            info: 'Please wait a few seconds. If the redirect does not work click',
            here: 'here',
            redirectInfo: 'You are being redirected...',
            welcome: 'Welcome to PSA!',
            slogan: 'Your sport event management system.',
            authProgress: 'Authentication in progress',
            wait: 'Please wait',
        },
    },

    alert: {
        great: 'Great!',
        info: 'Information!',
        attention: 'Attention!',
        warning: 'Warning!',
        error: 'Error!',
    },

    page: {
        groups: 'Groups',
        group: 'Group {{ name }}',
        event: 'Event',
        athletics: 'Athletics',
        results: 'Results',
        ranking: 'Ranking',
        management: 'Management',
        settings: 'Settings',
        users: 'Users',
    },

    form: {
        validation: {
            required: 'This field is required',
            pattern: 'This field does not follow the requirements',
        },
        action: {
            abort: 'Abort',
            save: 'Save',
            edit: 'Edit',
        },
    },

    label: {
        notDefined: 'N/D',
        actions: 'Actions',
        password: 'Password',
        changePassword: 'Change Password',
        logout: 'Sign Out',
        login: 'Sign In',
    },

    module: {
        confirmation: {
            confirm: 'Confirm',
        },
    },

    error: {
        noConnection: {
            title: 'No Connection to the server :(',
            text: 'PSA can not connect to the server. Please try later again. If the problem does not disappear contact your system administrator.',
            action: 'Try again',
        },
        notFound: {
            title: '404 Page not found',
            text: 'The page you want to load does not exist.',
            action: 'Take me home',
        },
    },

    // direct translations from a enum or a fix value in the application
    MALE: 'Male',
    FEMALE: 'Female',
    Athletics: 'Athletics',
};
