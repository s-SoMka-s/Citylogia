﻿using Citylogia.Server.Core.Entityes;
using Microsoft.EntityFrameworkCore;

namespace Citylogia.Server.Core.Db.Implementations
{
    public class SqlContext : DbContext
    {
        public SqlContext() : base() { }

        public SqlContext(DbContextOptions<SqlContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            
            builder.HasDefaultSchema("citylogia");


            #region Place
            {
                var place = builder.Entity<Place>();

                place.HasOne(p => p.Address)
                     .WithOne()
                     .OnDelete(DeleteBehavior.Cascade);

                place.HasMany(p => p.Photos)
                     .WithOne()
                     .OnDelete(DeleteBehavior.Cascade);

                place.HasMany(p => p.Reviews)
                     .WithOne()
                     .OnDelete(DeleteBehavior.Cascade);

                place.HasOne(p => p.Type)
                     .WithMany()
                     .OnDelete(DeleteBehavior.Cascade);
            }
            #endregion Place

            var address = builder.Entity<Address>();

            var review = builder.Entity<Review>();

            review.HasOne(r => r.Author)
                  .WithOne()
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