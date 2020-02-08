export const userListJsonSchema: object = {
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
            'username',
            'enabled',
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
            'username': {
                '$id': '#/items/properties/username',
                'type': 'string',
                'title': 'The Username Schema',
                'default': '',
                'examples': [
                    'mmuster',
                ],
                'pattern': '^(.*)$',
            },
            'enabled': {
                '$id': '#/items/properties/enabled',
                'type': 'boolean',
                'title': 'The Enabled Schema',
                'default': false,
                'examples': [
                    true,
                ],
            },
        },
    },
};

export const userJsonSchema: object = {
    'definitions': {},
    '$schema': 'http://json-schema.org/draft-07/schema#',
    '$id': 'http://example.com/root.json',
    'type': 'object',
    'title': 'The Root Schema',
    'required': [
        'id',
        'username',
        'enabled',
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
        'username': {
            '$id': '#/properties/username',
            'type': 'string',
            'title': 'The Username Schema',
            'default': '',
            'examples': [
                'mmuster',
            ],
            'pattern': '^(.*)$',
        },
        'enabled': {
            '$id': '#/properties/enabled',
            'type': 'boolean',
            'title': 'The Enabled Schema',
            'default': false,
            'examples': [
                true,
            ],
        },
    },
};
