// tslint:disable: max-line-length

export const competitorsJsonSchema: object = {
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
            'startNumber',
            'surname',
            'prename',
            'gender',
            'birthday',
            'absent',
            'address',
            'town',
            'group',
            'results',
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
            'startNumber': {
                '$id': '#/items/properties/startNumber',
                'type': 'integer',
                'title': 'The Startnumber Schema',
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
            'absent': {
                '$id': '#/items/properties/absent',
                'type': 'boolean',
                'title': 'The Absent Schema',
                'default': false,
                'examples': [
                    false,
                ],
            },
            'address': {
                '$id': '#/items/properties/address',
                'type': 'string',
                'title': 'The Address Schema',
                'default': '',
                'examples': [
                    'Musterstrasse 1',
                ],
                'pattern': '^(.*)$',
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
            'results': {
                '$id': '#/items/properties/results',
                'type': 'array',
                'title': 'The Results Schema',
                'items': {
                    '$id': '#/items/properties/results/items',
                    'type': 'object',
                    'title': 'The Items Schema',
                    'required': [
                        'id',
                        'value',
                        'points',
                        'distance',
                        'discipline',
                    ],
                    'properties': {
                        'id': {
                            '$id': '#/items/properties/results/items/properties/id',
                            'type': 'integer',
                            'title': 'The Id Schema',
                            'default': 0,
                            'examples': [
                                1,
                            ],
                        },
                        'value': {
                            '$id': '#/items/properties/results/items/properties/value',
                            'type': 'integer',
                            'title': 'The Value Schema',
                            'default': 0,
                            'examples': [
                                100,
                            ],
                        },
                        'points': {
                            '$id': '#/items/properties/results/items/properties/points',
                            'type': 'integer',
                            'title': 'The Points Schema',
                            'default': 0,
                            'examples': [
                                20,
                            ],
                        },
                        'distance': {
                            '$id': '#/items/properties/results/items/properties/distance',
                            'type': ['string', 'null'],
                            'title': 'The Distance Schema',
                            'default': '',
                            'examples': [
                                '2m',
                            ],
                            'pattern': '^(.*)$',
                        },
                        'discipline': {
                            '$id': '#/items/properties/results/items/properties/discipline',
                            'type': 'object',
                            'title': 'The Discipline Schema',
                            'required': [
                                'name',
                                'unit',
                            ],
                            'properties': {
                                'name': {
                                    '$id': '#/items/properties/results/items/properties/discipline/properties/name',
                                    'type': 'string',
                                    'title': 'The Name Schema',
                                    'default': '',
                                    'examples': [
                                        'Running',
                                    ],
                                    'pattern': '^(.*)$',
                                },
                                'unit': {
                                    '$id': '#/items/properties/results/items/properties/discipline/properties/unit',
                                    'type': 'object',
                                    'title': 'The Unit Schema',
                                    'required': [
                                        'name',
                                        'factor',
                                    ],
                                    'properties': {
                                        'name': {
                                            '$id': '#/items/properties/results/items/properties/discipline/properties/unit/properties/name',
                                            'type': 'string',
                                            'title': 'The Name Schema',
                                            'default': '',
                                            'examples': [
                                                '',
                                            ],
                                            'pattern': '^(.*)$',
                                        },
                                        'factor': {
                                            '$id': '#/items/properties/results/items/properties/discipline/properties/unit/properties/factor',
                                            'type': 'integer',
                                            'title': 'The Factor Schema',
                                            'default': 0,
                                            'examples': [
                                                1,
                                            ],
                                        },
                                    },
                                },
                            },
                        },
                    },
                },
            },
        },
    },
};

export const resultsJsonSchema: object = {
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
            'value',
            'points',
            'distance',
            'discipline',
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
            'value': {
                '$id': '#/items/properties/value',
                'type': 'integer',
                'title': 'The Value Schema',
                'default': 0,
                'examples': [
                    100,
                ],
            },
            'points': {
                '$id': '#/items/properties/points',
                'type': 'integer',
                'title': 'The Points Schema',
                'default': 0,
                'examples': [
                    20,
                ],
            },
            'distance': {
                '$id': '#/items/properties/distance',
                'type': ['string', 'null'],
                'title': 'The Distance Schema',
                'default': '',
                'examples': [
                    '2m',
                ],
                'pattern': '^(.*)$',
            },
            'discipline': {
                '$id': '#/items/properties/discipline',
                'type': 'object',
                'title': 'The Discipline Schema',
                'required': [
                    'name',
                    'unit',
                ],
                'properties': {
                    'name': {
                        '$id': '#/items/properties/discipline/properties/name',
                        'type': 'string',
                        'title': 'The Name Schema',
                        'default': '',
                        'examples': [
                            'Running',
                        ],
                        'pattern': '^(.*)$',
                    },
                    'unit': {
                        '$id': '#/items/properties/discipline/properties/unit',
                        'type': 'object',
                        'title': 'The Unit Schema',
                        'required': [
                            'name',
                            'factor',
                        ],
                        'properties': {
                            'name': {
                                '$id': '#/items/properties/discipline/properties/unit/properties/name',
                                'type': 'string',
                                'title': 'The Name Schema',
                                'default': '',
                                'examples': [
                                    '',
                                ],
                                'pattern': '^(.*)$',
                            },
                            'factor': {
                                '$id': '#/items/properties/discipline/properties/unit/properties/factor',
                                'type': 'integer',
                                'title': 'The Factor Schema',
                                'default': 0,
                                'examples': [
                                    1,
                                ],
                            },
                        },
                    },
                },
            },
        },
    },
};

export const disciplineListJsonSchema: object = {
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
            'name',
            'unit',
        ],
        'properties': {
            'name': {
                '$id': '#/items/properties/name',
                'type': 'string',
                'title': 'The Name Schema',
                'default': '',
                'examples': [
                    'Running',
                ],
                'pattern': '^(.*)$',
            },
            'unit': {
                '$id': '#/items/properties/unit',
                'type': 'object',
                'title': 'The Unit Schema',
                'required': [
                    'name',
                    'factor',
                ],
                'properties': {
                    'name': {
                        '$id': '#/items/properties/unit/properties/name',
                        'type': 'string',
                        'title': 'The Name Schema',
                        'default': '',
                        'examples': [
                            '',
                        ],
                        'pattern': '^(.*)$',
                    },
                    'factor': {
                        '$id': '#/items/properties/unit/properties/factor',
                        'type': 'integer',
                        'title': 'The Factor Schema',
                        'default': 0,
                        'examples': [
                            1,
                        ],
                    },
                },
            },
        },
    },
};
