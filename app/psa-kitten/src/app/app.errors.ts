/**
 * Indicates a connection issue whenever a http request can not be sent.
 *
 * @author Nicolas Märchy <billedtrain380@gmail.com>
 * @since 1.0.0
 */
export class NoConnectionError extends Error {

    constructor(message: string) {
        super(message);
        Object.setPrototypeOf(this, NoConnectionError.prototype);
    }
}
