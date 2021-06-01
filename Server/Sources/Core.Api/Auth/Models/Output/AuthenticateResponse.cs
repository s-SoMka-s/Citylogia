using Core.Tools.Interfaces.Auth;
using Newtonsoft.Json;

namespace Core.Api.Auth.Models.Output
{
    public class AuthenticateResponse
    {
        public AuthenticateResponse(TokenPair pair)
        {
            this.Access = new Token(pair.Access, pair.AccessExpiryDate);
            this.Refresh = new Token(pair.Refresh, pair.RefreshExpiryDate);
        }

        [JsonProperty("access")]
        public Token Access { get; set; }

        [JsonProperty("refresh")]
        public Token Refresh { get; set; }
    }
}
