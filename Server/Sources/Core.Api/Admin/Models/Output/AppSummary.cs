using Newtonsoft.Json;

namespace Core.Api.Admin.Models.Output
{
    public class AppSummary
    {

        public AppSummary(int newCount, int allCount)
        {
            this.PlacesSummary = new Summary(newCount, allCount);
            this.ReviewsSummary = new Summary(0, 0);
            this.UsersSummary = new Summary(0, 0);
        }

        [JsonProperty("users")]
        public Summary UsersSummary { get; set; }

        [JsonProperty("reviews")]
        public Summary ReviewsSummary { get; set; }

        [JsonProperty("places")]
        public Summary PlacesSummary { get; set; }

    }
}
