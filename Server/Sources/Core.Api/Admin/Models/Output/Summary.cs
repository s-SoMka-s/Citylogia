using Newtonsoft.Json;

namespace Core.Api.Admin.Models.Output
{
    public class Summary
    {
        public Summary(int newCount, int allCount)
        {
            this.NewCount = newCount;
            this.AllCount = allCount;
        }

        [JsonProperty("new_count")]
        public long NewCount { get; set; }

        [JsonProperty("all_count")]
        public long AllCount { get; set; }
    }
}