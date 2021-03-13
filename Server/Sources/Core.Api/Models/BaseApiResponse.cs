using Newtonsoft.Json;


namespace Core.Api.Models
{
    public class BaseApiResponse<T>
    {
        public BaseApiResponse(long code, T data)
        {
            this.StatusCode = code;
            this.Data = data;
        }


        [JsonProperty("status_code")]
        public long StatusCode { get; set; }

        [JsonProperty("data")]
        public T Data { get; set; }
    }
}
