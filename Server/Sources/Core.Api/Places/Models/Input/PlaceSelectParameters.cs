using Microsoft.AspNetCore.Mvc;
using System;

namespace Core.Api.Places.Models.Input
{
    public class PlaceSelectParameters
    {
        [FromQuery(Name = "type_ids")]
        public long[] TypeIds { get; set; } = Array.Empty<long>();

        [FromQuery(Name = "take")]
        public long? Take { get; set; }

        [FromQuery(Name = "skip")]
        public long? Skip { get; set; }

        [FromQuery(Name = "only_approved")]
        public bool OnlyApproved { get; set; } = false;

        [FromQuery(Name = "only_not_reviewed")]
        public bool OnlyNotReviewed { get; set; } = false;
    }
}
