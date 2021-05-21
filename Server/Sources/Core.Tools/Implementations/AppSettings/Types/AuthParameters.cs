using Core.Tools.Interfaces.AppSettings.Types;

namespace Core.Tools.Implementations.AppSettings.Types
{
    public class AuthParameters : IAuthParameters
    {
        public string Issuer { get; set; }
        public string Key { get; set; }
        public int AccessTokenLifetimeInMinutes { get; set; }
        public int RefreshTokenLifetimeInMinutes { get; set; }
        public int ResetPasswordRequestLifetimeInHours { get; set; }
        public int TemporaryUserLifetimeInHours { get; set; }
    }
}
