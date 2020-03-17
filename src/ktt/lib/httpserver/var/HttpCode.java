package ktt.lib.httpserver.var;

/**
 * A list of expected HTTP status codes.
 * See <a href="https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml" target="_blank">IANA RFC7231</a>
 * @since 01.00.00
 * @version 01.00.00
 * @author Ktt Development
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public abstract class HttpCode {

    public static final int HTTP_Continue = 100;
    public static final int HTTP_SwitchingProtocols = 101;
    public static final int HTTP_Processing = 102;
    public static final int HTTP_EarlyHints = 103 ;
    // 104-199 Unassigned
    public static final int HTTP_OK = 200;
    public static final int HTTP_Created = 201;
    public static final int HTTP_Accepted = 202;
    public static final int HTTP_NonAuthoritativeInformation = 203;
    public static final int HTTP_NoContent = 204;
    public static final int HTTP_ResetContent = 205;
    public static final int HTTP_PartialContent = 206;
    public static final int HTTP_MultiStatus = 207;
    public static final int HTTP_AlreadyReported = 208;
    // 209-225 Unassigned
    public static final int HTTP_IMUsed = 226;
    // 227-299 Unassigned
    public static final int HTTP_MultipleChoices = 300;
    public static final int HTTP_MovedPermanently = 301;
    public static final int HTTP_Found = 302;
    public static final int HTTP_SeeOther = 303;
    public static final int HTTP_NotModified = 304;
    public static final int HTTP_UseProxy = 305;
    public static final int HTTP_Unused = 306;
    public static final int HTTP_TemporaryRedirect = 307;
    public static final int HTTP_PermanentRedirect = 308;
    // 309-399 Unassigned
    public static final int HTTP_BadRequest = 400;
    public static final int HTTP_Unauthorized = 401;
    public static final int HTTP_PaymentRequired = 402;
    public static final int HTTP_Forbidden = 403;
    public static final int HTTP_NotFound = 404;
    public static final int HTTP_MethodNotAllowed = 405;
    public static final int HTTP_NotAcceptable = 406;
    public static final int HTTP_ProxyAuthenticationRequired = 407;
    public static final int HTTP_RequestTimeout = 408;
    public static final int HTTP_Conflict = 409;
    public static final int HTTP_Gone = 410;
    public static final int HTTP_LengthRequired = 411;
    public static final int HTTP_PreconditionFailed = 412;
    public static final int HTTP_PayloadTooLarge = 413;
    public static final int HTTP_URITooLong = 414;
    public static final int HTTP_UnsupportedMediaType = 415;
    public static final int HTTP_RangeNotSatisfiable = 416;
    public static final int HTTP_ExpectationFailed = 417;
    // 418-420 Unassigned
    public static final int HTTP_MisdirectedRequest = 421;
    public static final int HTTP_UnprocessableEntity = 422;
    public static final int HTTP_Locked = 423;
    public static final int HTTP_FailedDependency = 424;
    public static final int HTTP_TooEarly = 425;
    public static final int HTTP_UpgradeRequired = 426;
    // 427 Unassigned
    public static final int HTTP_PreconditionRequired = 428;
    public static final int HTTP_TooManyRequests = 429;
    // 430 Unassigned
    public static final int HTTP_RequestHeaderFieldsTooLarge = 431;
    // 432-450 Unassigned
    public static final int HTTP_UnavailableForLegalReasons = 451;
    // 452-499 Unassigned
    public static final int HTTP_InternalServerError = 500;
    public static final int HTTP_NotImplemented = 501;
    public static final int HTTP_BadGateway = 502;
    public static final int HTTP_ServiceUnavailable = 503;
    public static final int HTTP_GatewayTimeout = 504;
    public static final int HTTP_HTTPVersionNotSupported = 505;
    public static final int HTTP_VariantAlsoNegotiates = 506;
    public static final int HTTP_InsufficientStorage = 507;
    public static final int HTTP_LoopDetected = 508;
    // 509 Unassigned
    public static final int HTTP_NotExtended = 510;
    public static final int HTTP_NetworkAuthenticationRequired = 511;
    // 512-599 Unassigned

}
