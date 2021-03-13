using Newtonsoft.Json;

namespace Citylogia.Server.Models
{
    public class BaseApiResponse<T>
    {
        public BaseApiResponse(long statusCode, T data)
        {
            this.StatusCode = statusCode;
            this.Data = data;
        }


        [JsonProperty("data")]
        public T Data { get; set; }

        [JsonProperty("status_code")]
        public long StatusCode { get; set; }
    }
}
