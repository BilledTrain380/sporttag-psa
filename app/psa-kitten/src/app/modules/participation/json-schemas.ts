export const participationStatusJsonSchema: object = {
    'definitions': {},
    '$schema': 'http://json-schema.org/draft-07/schema#',
    '$id': 'http://example.com/root.json',
    'type': 'object',
    'title': 'The Root Schema',
    'required': [
        'status',
    ],
    'properties': {
        'status': {
            '$id': '#/properties/status',
            'type': 'string',
            'title': 'The Status Schema',
            'default': 'OPEN',
            'enum': ['OPEN', 'CLOSE', 'RESET'],
            'examples': [
                'OPEN',
            ],
            'pattern': '^(.*)$',
        },
    },
};
