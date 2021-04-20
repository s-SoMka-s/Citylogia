using Newtonsoft.Json;

namespace Core.Api.Auth.Models.Output
{
    public class Token
    {
        public Token(string token)
        {
            this.AccessToken = token;
        }


        [JsonProperty("access_token")]
        public string AccessToken { get; set; }
    }
}
