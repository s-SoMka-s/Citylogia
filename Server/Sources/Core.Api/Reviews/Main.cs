using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Reviews
{
    [ApiController]
    [Route("/api/Reviews")]
    public class Main
    {
        private readonly SqlContext context;
        public Main(SqlContext context)
        {
            this.context = context;
        }

        [HttpGet("{id}")]
        public async Task<BaseCollectionResponse<ReviewSummary>> GetReviews(long id)
        {
            var reviews = await this.Query()
                                    .Where(r => r.PlaceId == id)
                                    .ToArrayAsync();

            var summaries = new List<ReviewSummary>();

            foreach (var review in reviews)
            {
                var res = new ReviewSummary(review);
                summaries.Add(res);
            }

            var response = new BaseCollectionResponse<ReviewSummary>(summaries);

            return response;
        }

        [HttpPost("{placeId}")]
        public async Task<bool> AddReviewAsync(long placeId, [FromBody] ReviewInputParameters parameters)
        {
            var @new = parameters.Build();
            var place = this.context.Places.FirstOrDefault(p => p.Id == placeId);
            var author = this.context.Users.FirstOrDefault(u => u.Id == parameters.UserId);
            @new.PlaceId = place.Id;
            @new.UserId = author.Id;
            await this.context.Reviews.AddAsync(@new);
            await this.context.SaveChangesAsync();

            return true;
        }

        private IQueryable<Review> Query()
        {
            return this.context
                       .Reviews
                       .Include(r => r.Place)
                       .Include(r => r.Author);
        }
    }
}
