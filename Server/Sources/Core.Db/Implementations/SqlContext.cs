using Citylogia.Server.Core.Entityes;
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



        public SqlContext() : base() { }

        public SqlContext(DbContextOptions<SqlContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            
            builder.HasDefaultSchema("citylogia");


            #region Place
            {
                var place = builder.Entity<Place>();

                place.HasMany(p => p.Photos)
                     .WithOne()
                     .OnDelete(DeleteBehavior.Cascade);

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

            var placeType = builder.Entity<PlaceType>();

            var user = builder.Entity<User>();

            user.HasOne(u => u.Avatar)
                .WithOne()
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
