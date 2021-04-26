using Citylogia.Server.Core.Entityes;
using Core.Entities;
using Microsoft.EntityFrameworkCore;

namespace Citylogia.Server.Core.Db.Implementations
{
    public class SqlContext : DbContext
    {
        public DbSet<Place> Places { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<Photo> Photos { get; set; }
        public DbSet<PlaceType> PlaceTypes { get; set; }
        public DbSet<FavoritePlaceLink> FavoritePlaceLinks { get; set; }

        public SqlContext() : base() { }

        public SqlContext(DbContextOptions<SqlContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            
            builder.HasDefaultSchema("citylogia");


            #region Place
            {
                var place = builder.Entity<Place>();

                place.HasOne(p => p.Type)
                     .WithMany()
                     .OnDelete(DeleteBehavior.Cascade);
            }
            #endregion Place


            var review = builder.Entity<Review>();

            review.HasOne(r => r.Author)
                  .WithMany()
                  .OnDelete(DeleteBehavior.Cascade);

            review.HasOne(r => r.Place)
                  .WithMany(p => p.Reviews)
                  .OnDelete(DeleteBehavior.Cascade);

            var photo = builder.Entity<Photo>();

            photo.HasOne(p => p.Place)
                .WithMany(p => p.Photos)
                .OnDelete(DeleteBehavior.Cascade);

            var placeType = builder.Entity<PlaceType>();

            var user = builder.Entity<User>();

            user.HasOne(u => u.Avatar)
                .WithOne()
                .OnDelete(DeleteBehavior.Cascade);

            #region Favorites
            {
                var link = builder.Entity<FavoritePlaceLink>();

                link.HasOne(l => l.Place)
                    .WithMany()
                    .OnDelete(DeleteBehavior.Cascade);

                link.HasOne(l => l.User)
                    .WithMany()
                    .OnDelete(DeleteBehavior.Cascade);
            }
            #endregion Favorites
        }
    }
}
