/**
 * Indicates an error regarding any kind of request errors.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export class RequestError extends Error {

    constructor(
        message: string,
        readonly timestamp: string,
        readonly status: number,
        readonly statusText: string,
        readonly path: string,
    ) {
        super(message);
        Object.setPrototypeOf(this, RequestError.prototype);
    }
}


/**
 * Indicates an error regarding any kind of authentication failure.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export class AuthenticationError extends RequestError {

    constructor(
        message: string,
        timestamp: string,
        path: string,
    ) {
        super(message, timestamp, 401, 'Not Authenticated', path);
        Object.setPrototypeOf(this, AuthenticationError.prototype);
    }
}

/**
 * Indicates an error regarding any resource which does not exist.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export class ResourceNotFoundError extends RequestError {

    constructor(
        message: string,
        timestamp: string,
        path: string,
    ) {
        super(message, timestamp, 404, 'Not Found', path);
        Object.setPrototypeOf(this, ResourceNotFoundError.prototype);
    }
}

/**
 * Indicates an error regarding any kind of bad request.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export class BadRequestError extends RequestError {

    constructor(
        message: string,
        timestamp: string,
        path: string,
    ) {
        super(message, timestamp, 400, 'Bad Request', path);
        Object.setPrototypeOf(this, BadRequestError.prototype);
    }
}
