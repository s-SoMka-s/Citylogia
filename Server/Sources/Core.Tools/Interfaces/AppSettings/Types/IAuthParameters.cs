namespace Core.Tools.Interfaces.AppSettings.Types
{
    public interface IAuthParameters
    {
        string Issuer { get; }
        string Key { get; }
        int AccessTokenLifetimeInMinutes { get; }
        int RefreshTokenLifetimeInMinutes { get; }
        int ResetPasswordRequestLifetimeInHours { get; }
        int TemporaryUserLifetimeInHours { get; }
    }
}
