export const groupsJsonSchema: object = {
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
            'coach',
        ],
        'properties': {
            'name': {
                '$id': '#/items/properties/name',
                'type': 'string',
                'title': 'The Name Schema',
                'default': '',
                'examples': [
                    '2a',
                ],
                'pattern': '^(.*)$',
            },
            'coach': {
                '$id': '#/items/properties/coach',
                'type': 'object',
                'title': 'The Coach Schema',
                'required': [
                    'id',
                    'name',
                ],
                'properties': {
                    'id': {
                        '$id': '#/items/properties/coach/properties/id',
                        'type': 'integer',
                        'title': 'The Id Schema',
                        'default': 0,
                        'examples': [
                            1,
                        ],
                    },
                    'name': {
                        '$id': '#/items/properties/coach/properties/name',
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
};

export const groupJsonSchema: object = {
    'definitions': {},
    '$schema': 'http://json-schema.org/draft-07/schema#',
    '$id': 'http://example.com/root.json',
    'type': 'object',
    'title': 'The Root Schema',
    'required': [
        'name',
        'coach',
    ],
    'properties': {
        'name': {
            '$id': '#/properties/name',
            'type': 'string',
            'title': 'The Name Schema',
            'default': '',
            'examples': [
                '2a',
            ],
            'pattern': '^(.*)$',
        },
        'coach': {
            '$id': '#/properties/coach',
            'type': 'object',
            'title': 'The Coach Schema',
            'required': [
                'id',
                'name',
            ],
            'properties': {
                'id': {
                    '$id': '#/properties/coach/properties/id',
                    'type': 'integer',
                    'title': 'The Id Schema',
                    'default': 0,
                    'examples': [
                        1,
                    ],
                },
                'name': {
                    '$id': '#/properties/coach/properties/name',
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
};
