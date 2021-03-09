using Citylogia.Server.Entityes;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Citylogia.Server
{
    public class ApplicationContext : DbContext
    {
        public DbSet<Place> Places { get; set; }
        private const string host = "84.201.147.252";
        private const string port = "5432";
        private const string database = "citylogia";
        private const string userName = "postgres";
        private const string password = "12345";

        public ApplicationContext() { }

        protected override void OnConfiguring(DbContextOptionsBuilder builder)
        {
            builder.UseNpgsql($"Host={host};Port={port};Database={database};Username={userName};Password={password};");
        }
    }
}
