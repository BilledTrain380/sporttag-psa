export const sportListJsonSchema: object = {
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
        ],
        'properties': {
            'name': {
                '$id': '#/items/properties/name',
                'type': 'string',
                'title': 'The Name Schema',
                'default': '',
                'examples': [
                    'Athletics',
                ],
                'pattern': '^(.*)$',
            },
        },
    },
};
