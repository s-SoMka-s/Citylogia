using Newtonsoft.Json;

namespace Core.Api.Auth.Models.Output
{
    public class AuthenticateResponse
    {
        public AuthenticateResponse(string token)
        {
            Token = token;
        }

        [JsonProperty("Token")]
        public string Token { get; set; }
    }
}
