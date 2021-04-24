using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Core.Api.Users
{
    [Route("/api/Users")]
    [ApiController]
    public class Main : Controller
    {
        private readonly SqlContext context;

        public Main(SqlContext context)
        {
            this.context = context;
        }


        [HttpGet("")]
        public async Task<BaseCollectionResponse<UserSummary>> GetUsersAsync()
        {
            var query = this.Query();

            var summaries = await query.Select(u => new UserSummary(u))
                                       .ToListAsync();

            return new BaseCollectionResponse<UserSummary>(summaries);
        }


        private IQueryable<User> Query()
        {
            return this.context.Users;
        }
    }
}
