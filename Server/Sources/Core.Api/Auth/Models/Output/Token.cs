using Newtonsoft.Json;

namespace Core.Api.Auth.Models.Output
{
    public class Token
    {
        public Token(string token, long expiry)
        {
            this.TokenValue = token;
            this.Expiry = expiry;
        }


        [JsonProperty("token")]
        public string TokenValue { get; set; }

        [JsonProperty("expiry")]
        public long Expiry { get; }
    }
}
