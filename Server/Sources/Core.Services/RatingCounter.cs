using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Libraries.Db.Reposiitory.Interfaces;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Hosting;
using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Core.Services
{
    public class RatingCounter : BackgroundService
    {
        private readonly ICrudRepository<Place> places;
        private readonly SqlContext context;
        private readonly TimeSpan period = TimeSpan.FromHours(2);


        public RatingCounter(ICrudFactory factory, SqlContext context)
        {
            this.places = factory.Get<Place>();
            this.context = context;
        }

        protected override async Task ExecuteAsync(CancellationToken cancellationToken)
        {
            while(!cancellationToken.IsCancellationRequested)
            {
                try
                {
                    if (cancellationToken.IsCancellationRequested)
                    {
                        break;
                    }

                    var places = await Query().ToArrayAsync();
                    foreach (var place in places)
                    {
                        await this.recountRatingAsync(place);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                    break;
                }

                await Task.Delay(period, cancellationToken);
            }
        }

        private async Task recountRatingAsync(Place place)
        {
            var sum = 0L;
            var count = place.Reviews.Count;

            if (count == 0)
            {
                return;
            }

            var reviews = place.Reviews.ToArray();
            foreach(var review in reviews)
            {
                sum += review.Mark;
            }

            place.Mark = sum / count;

            this.context.Places.Update(place);
            await this.context.SaveChangesAsync();
        }

        private IQueryable<Place> Query()
        {
            return this.places.Query().Include(p => p.Reviews);
        }
    }
}
