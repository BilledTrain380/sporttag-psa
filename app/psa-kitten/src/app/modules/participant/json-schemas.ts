export const participantListJsonSchema: object = {
    'definitions': {},
    '$schema': 'http://json-schema.org/draft-07/schema#',
    '$id': 'http://example.com/root.json',
    'type': 'array',
    'title': 'The Root Schema',
    'items': {
        '$id': '#/items',
        'type': 'object',
        'title': 'The Items Schema',
        'required': [
            'id',
            'surname',
            'prename',
            'gender',
            'birthday',
            'address',
            'absent',
            'town',
            'group',
            'sport',
        ],
        'properties': {
            'id': {
                '$id': '#/items/properties/id',
                'type': 'integer',
                'title': 'The Id Schema',
                'default': 0,
                'examples': [
                    1,
                ],
            },
            'surname': {
                '$id': '#/items/properties/surname',
                'type': 'string',
                'title': 'The Surname Schema',
                'default': '',
                'examples': [
                    'Müller',
                ],
                'pattern': '^(.*)$',
            },
            'prename': {
                '$id': '#/items/properties/prename',
                'type': 'string',
                'title': 'The Prename Schema',
                'default': '',
                'examples': [
                    'Max',
                ],
                'pattern': '^(.*)$',
            },
            'gender': {
                '$id': '#/items/properties/gender',
                'type': 'string',
                'title': 'The Gender Schema',
                'default': '',
                'examples': [
                    'MALE',
                ],
                'pattern': '^(.*)$',
            },
            'birthday': {
                '$id': '#/items/properties/birthday',
                'type': 'integer',
                'title': 'The Birthday Schema',
                'default': 0,
                'examples': [
                    1529234942,
                ],
            },
            'address': {
                '$id': '#/items/properties/address',
                'type': 'string',
                'title': 'The Address Schema',
                'default': '',
                'examples': [
                    'Examplestreet 5',
                ],
                'pattern': '^(.*)$',
            },
            'absent': {
                '$id': '#/items/properties/absent',
                'type': 'boolean',
                'title': 'The Absent Schema',
                'default': false,
                'examples': [
                    false,
                ],
            },
            'town': {
                '$id': '#/items/properties/town',
                'type': 'object',
                'title': 'The Town Schema',
                'required': [
                    'zip',
                    'name',
                ],
                'properties': {
                    'zip': {
                        '$id': '#/items/properties/town/properties/zip',
                        'type': 'string',
                        'title': 'The Zip Schema',
                        'default': '',
                        'examples': [
                            '8000',
                        ],
                        'pattern': '^(.*)$',
                    },
                    'name': {
                        '$id': '#/items/properties/town/properties/name',
                        'type': 'string',
                        'title': 'The Name Schema',
                        'default': '',
                        'examples': [
                            'Zürich',
                        ],
                        'pattern': '^(.*)$',
                    },
                },
            },
            'group': {
                '$id': '#/items/properties/group',
                'type': 'object',
                'title': 'The Group Schema',
                'required': [
                    'name',
                    'coach',
                ],
                'properties': {
                    'name': {
                        '$id': '#/items/properties/group/properties/name',
                        'type': 'string',
                        'title': 'The Name Schema',
                        'default': '',
                        'examples': [
                            '2a',
                        ],
                        'pattern': '^(.*)$',
                    },
                    'coach': {
                        '$id': '#/items/properties/group/properties/coach',
                        'type': 'object',
                        'title': 'The Coach Schema',
                        'required': [
                            'id',
                            'name',
                        ],
                        'properties': {
                            'id': {
                                '$id': '#/items/properties/group/properties/coach/properties/id',
                                'type': 'integer',
                                'title': 'The Id Schema',
                                'default': 0,
                                'examples': [
                                    1,
                                ],
                            },
                            'name': {
                                '$id': '#/items/properties/group/properties/coach/properties/name',
                                'type': 'string',
                                'title': 'The Name Schema',
                                'default': '',
                                'examples': [
                                    'Max Muster',
                                ],
                                'pattern': '^(.*)$',
                            },
                        },
                    },
                },
            },
            'sport': {
                '$id': '#/items/properties/sport',
                'type': ['object', 'null'],
                'title': 'The Sport Schema',
                'required': [
                    'name',
                ],
                'properties': {
                    'name': {
                        '$id': '#/items/properties/sport/properties/name',
                        'type': 'string',
                        'title': 'The Name Schema',
                        'default': '',
                        'examples': [
                            '',
                        ],
                        'pattern': '^(.*)$',
                    },
                },
            },
        },
    },
};

export const participantJsonSchema: object = {
    'definitions': {},
    '$schema': 'http://json-schema.org/draft-07/schema#',
    '$id': 'http://example.com/root.json',
    'type': 'object',
    'title': 'The Root Schema',
    'required': [
        'id',
        'surname',
        'prename',
        'gender',
        'birthday',
        'absent',
        'address',
        'town',
        'group',
        'sport',
    ],
    'properties': {
        'id': {
            '$id': '#/properties/id',
            'type': 'integer',
            'title': 'The Id Schema',
            'default': 0,
            'examples': [
                1,
            ],
        },
        'surname': {
            '$id': '#/properties/surname',
            'type': 'string',
            'title': 'The Surname Schema',
            'default': '',
            'examples': [
                'Müller',
            ],
            'pattern': '^(.*)$',
        },
        'prename': {
            '$id': '#/properties/prename',
            'type': 'string',
            'title': 'The Prename Schema',
            'default': '',
            'examples': [
                'Max',
            ],
            'pattern': '^(.*)$',
        },
        'gender': {
            '$id': '#/properties/gender',
            'type': 'string',
            'title': 'The Gender Schema',
            'default': '',
            'examples': [
                'MALE',
            ],
            'pattern': '^(.*)$',
        },
        'birthday': {
            '$id': '#/properties/birthday',
            'type': 'integer',
            'title': 'The Birthday Schema',
            'default': 0,
            'examples': [
                1529234942,
            ],
        },
        'absent': {
            '$id': '#/properties/absent',
            'type': 'boolean',
            'title': 'The Absent Schema',
            'default': false,
            'examples': [
                false,
            ],
        },
        'address': {
            '$id': '#/properties/address',
            'type': 'string',
            'title': 'The Address Schema',
            'default': '',
            'examples': [
                'Examplestreet 5',
            ],
            'pattern': '^(.*)$',
        },
        'town': {
            '$id': '#/properties/town',
            'type': 'object',
            'title': 'The Town Schema',
            'required': [
                'zip',
                'name',
            ],
            'properties': {
                'zip': {
                    '$id': '#/properties/town/properties/zip',
                    'type': 'string',
                    'title': 'The Zip Schema',
                    'default': '',
                    'examples': [
                        '8000',
                    ],
                    'pattern': '^(.*)$',
                },
                'name': {
                    '$id': '#/properties/town/properties/name',
                    'type': 'string',
                    'title': 'The Name Schema',
                    'default': '',
                    'examples': [
                        'Zürich',
                    ],
                    'pattern': '^(.*)$',
                },
            },
        },
        'group': {
            '$id': '#/properties/group',
            'type': 'object',
            'title': 'The Group Schema',
            'required': [
                'name',
                'coach',
            ],
            'properties': {
                'name': {
                    '$id': '#/properties/group/properties/name',
                    'type': 'string',
                    'title': 'The Name Schema',
                    'default': '',
                    'examples': [
                        '2a',
                    ],
                    'pattern': '^(.*)$',
                },
                'coach': {
                    '$id': '#/properties/group/properties/coach',
                    'type': 'object',
                    'title': 'The Coach Schema',
                    'required': [
                        'id',
                        'name',
                    ],
                    'properties': {
                        'id': {
                            '$id': '#/properties/group/properties/coach/properties/id',
                            'type': 'integer',
                            'title': 'The Id Schema',
                            'default': 0,
                            'examples': [
                                1,
                            ],
                        },
                        'name': {
                            '$id': '#/properties/group/properties/coach/properties/name',
                            'type': 'string',
                            'title': 'The Name Schema',
                            'default': '',
                            'examples': [
                                'Max Muster',
                            ],
                            'pattern': '^(.*)$',
                        },
                    },
                },
            },
        },
        'sport': {
            '$id': '#/properties/sport',
            'type': ['object', 'null'],
            'title': 'The Sport Schema',
            'required': [
                'name',
            ],
            'properties': {
                'name': {
                    '$id': '#/properties/sport/properties/name',
                    'type': 'string',
                    'title': 'The Name Schema',
                    'default': '',
                    'examples': [
                        '',
                    ],
                    'pattern': '^(.*)$',
                },
            },
        },
    },
};
